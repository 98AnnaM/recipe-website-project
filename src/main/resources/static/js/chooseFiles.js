document.getElementById('formFileMultiple').addEventListener('change', function() {
    var selectedFilesDiv = document.getElementById('selectedFiles');

    for (var i = 0; i < this.files.length; i++) {
        var file = this.files[i];
        var fileItem = document.createElement('div');
        fileItem.classList.add('d-flex', 'align-items-center', 'mb-2');
        fileItem.innerHTML = `
            <span>${file.name}</span>
            <button type="button" class="btn btn-light btn-custom ml-2 remove-file" data-index="${i}">
                <span aria-hidden="true">&times;</span>
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