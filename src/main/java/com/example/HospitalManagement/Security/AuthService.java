package com.example.HospitalManagement.Security;

import com.example.HospitalManagement.Entity.Patient;
import com.example.HospitalManagement.Entity.Type.AuthProviderType;
import com.example.HospitalManagement.Entity.Type.RoleType;
import com.example.HospitalManagement.Entity.User;
import com.example.HospitalManagement.Repositary.PatientRepositary;
import com.example.HospitalManagement.Repositary.UserRepository;
import com.example.HospitalManagement.dto.LoginRequestDto;
import com.example.HospitalManagement.dto.LoginResponseDto;
import com.example.HospitalManagement.dto.SignupRequestDto;
import com.example.HospitalManagement.dto.SignupResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PatientRepositary patientRepositary;

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(),loginRequestDto.getPassword())
        );

        User user = (User) authentication.getPrincipal();

        String token = authUtil.generateAccessTokens(user);

        return new LoginResponseDto(token,user.getId());
    }

    public User signupInternal(SignupRequestDto signupRequestDto,AuthProviderType providerType,String providerId){
        User user = userRepository.findByUsername(signupRequestDto.getUsername()).orElse(null);

        if (user != null) throw new IllegalArgumentException("User Already Exists!");

        user = userRepository.save(User
                .builder()
                .username(signupRequestDto.getUsername())
                .providerType(providerType)
                .role(signupRequestDto.getRoles()) // Role.PATIENT
                .providerId(providerId)
                .build());

        if (providerType == AuthProviderType.EMAIL ){
             user.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
        }

        user = userRepository.save(user);

        Patient patient = Patient.builder()
                .name(signupRequestDto.getName())
                .email(signupRequestDto.getUsername())
                .user(user)
                .build();

        patientRepositary.save(patient);

        return user;
    }

    // controller Login
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        User user = signupInternal(signupRequestDto,AuthProviderType.EMAIL,null);

        return new SignupResponseDto(user.getId(), user.getUsername());
    }

    @Transactional
    public ResponseEntity<LoginResponseDto> handleOAuth2LoginService(OAuth2User oAuth2User, String registrationId) {
        // fetch Provider type and Provider Id
        AuthProviderType providerType = authUtil.getProviderTypeFromRegistrationId(registrationId);
        String providerId = authUtil.determineProviderIdFromOAuth2User(oAuth2User,registrationId);

        User user = userRepository.findByProviderTypeAndProviderId(providerType,providerId).orElse(null);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        User emailUser = userRepository.findByUsername(email).orElse(null);

        if (user == null && emailUser == null){
            // SignUp Flow
            String username = authUtil.determineUsernameFromOAuth2User(oAuth2User,registrationId,providerId);
            user = signupInternal(new SignupRequestDto(username,null,name,Set.of(RoleType.PATIENT)),providerType,providerId);
        }else if (user != null){
            if (email != null && !email.isBlank() && !email.equals(user.getUsername())){
                user.setUsername(email);
                userRepository.save(user);
            }
        }else{ // User not exist with OAuth2 provider but exist with emailUser
            throw new BadCredentialsException("This email is already registered with provider : "+emailUser.getProviderType());
        }

        // Login
        LoginResponseDto loginResponseDto = new LoginResponseDto(authUtil.generateAccessTokens(user),user.getId());


        return ResponseEntity.ok(loginResponseDto);
        // save the Provider type and Provider Id info with user
        // If the user has account : Directly Login

        // Otherwise, first SignUp and then Login
    }
}
