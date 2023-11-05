function validateAndUploadFile() {
    var fileInput = document.getElementById('picture');
    var file = fileInput.files[0];
    var allowedTypes = ['image/jpeg', 'image/png', 'image/gif']; // Add more allowed types if needed

    if (allowedTypes.indexOf(file.type) === -1) {
        alert('Please upload an image file (JPEG, PNG, GIF).');
        fileInput.value = ''; // Clear the file input
        return;
    }

    var form = document.getElementById('uploadForm');
    form.submit();
}