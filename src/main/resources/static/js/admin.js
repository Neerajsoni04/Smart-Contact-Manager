console.log("admin user");

document
  .querySelector("#image_file_input")
  .addEventListener("change", function (event) {
    let file = event.target.files[0]; // Get the selected file
    let reader = new FileReader();  // Create a new FileReader object
    reader.onload = function () {  // When the file is read successfully
      // Set the src attribute of the image preview element to the file's data URL
      document
        .querySelector("#upload_image_preview")
        .setAttribute("src", reader.result);
    };
    reader.readAsDataURL(file); // Starts reading the file as a Data URL (base64 string) 
                                // This is necessary because image src attributes can accept base64-encoded image data.
  });