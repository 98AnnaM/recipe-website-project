
// Validate and upload new picture
function validateAndUploadFile() {
    var fileInput = document.getElementById('picture');
    var file = fileInput.files[0];
    var allowedTypes = ['image/jpeg', 'image/png', 'image/gif']; // Add more allowed types if needed

    if (allowedTypes.indexOf(file.type) === -1) {
        alert('Please upload an image file (JPEG, PNG, GIF).');
        fileInput.value = ''; // Clear the file input
        return;
    }

    document.getElementById('overlay').style.display = 'flex';
    document.getElementById('spinner').style.display = 'block';

    var form = document.getElementById('uploadForm');
    form.submit();
}

function showDeleteSpinner(event) {
    // Show the overlay and spinner
    document.getElementById('overlay').style.display = 'flex';
    document.getElementById('spinner').style.display = 'block';

    var deleteButton = event.target.querySelector('input[type="submit"]');
    deleteButton.disabled = true;
}




