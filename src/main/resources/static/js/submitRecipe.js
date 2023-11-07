document.getElementById('formFileMultiple').addEventListener('change', function() {
    var selectedFilesDiv = document.getElementById('selectedFiles');
    var allowedTypes = ['image/jpeg', 'image/png', 'image/gif'];

    for (var i = 0; i < this.files.length; i++) {
        var file = this.files[i];

        if (allowedTypes.indexOf(file.type) === -1) {
            alert('Please upload an image file (JPEG, PNG, GIF).');
            continue; // Skip this file
        }

        var fileItem = document.createElement('div');
        fileItem.classList.add('d-flex', 'align-items-center', 'mb-2');
        fileItem.innerHTML = `
            <span>${file.name}</span>
            <button type="button" class="btn btn-light p-0 lh-1 text-md ml-2 remove-file" data-index="${i}" style="width: 1.2rem; height: 1.2rem; line-height: 1;">
               <span aria-hidden="true" class="text-md">&times;</span>
            </button>
        `;
        selectedFilesDiv.appendChild(fileItem);
    }

    // Add event listener to remove files
    var removeButtons = document.getElementsByClassName('remove-file');
    for (var j = 0; j < removeButtons.length; j++) {
        removeButtons[j].addEventListener('click', function() {
            var index = this.getAttribute('data-index');
            this.parentElement.remove(); // Remove the file item from the list
        });
    }
});

//Add logic for showing spinner
document.getElementById('recipeForm').addEventListener('submit', function(event) {
    // Show the overlay and spinner
    document.getElementById('overlay').style.display = 'flex';
    document.getElementById('spinner').style.display = 'block';

    // Disable the submit button to prevent multiple submissions
    document.getElementById('submitBtn').disabled = true;
});