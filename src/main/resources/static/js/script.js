$(document).ready(function(){
    // Function to change the main image
    function changeMainImage(src, pictureId, canNotDelete) {
        $('#mainImage').attr('src', src);
        $('#mainPictureId').val(pictureId); // Use .val() to set the value
        var submitButton = $('#deleteMainImageButton');
        if (canNotDelete) {
            submitButton.prop('disabled', true); // Disable the submit button
        } else {
            submitButton.prop('disabled', false); // Enable the submit button
        }
    }

    function updateMainImageOnClick(element) {
        var src = $(element).attr('src');
        var pictureId = $(element).data('picture-id');
        var canNotDelete = $(element).data('can-not-delete');
        changeMainImage(src, pictureId, canNotDelete);
    }

    // Set the initial image to the first small image
    var initialImage = $('.small-images img:first').attr('src');
    var initialPictureId = $('.small-images img:first').data('picture-id');
    var initialCanNotDelete = $('.small-images img:first').data('can-not-delete');
    changeMainImage(initialImage, initialPictureId, initialCanNotDelete);

    // Clicking on small images should change the main image
    $('.small-images img').click(function(){
        updateMainImageOnClick(this);
    });

    // Manual navigation buttons
    $('#prevButton').click(function(){
        var currentImage = $('#mainImage').attr('src');
        var prevImage = $('.small-images img[src="'+currentImage+'"]').parent().prev().find('img');
        if (prevImage.length > 0) {
            updateMainImageOnClick(prevImage);
        }
    });

    $('#nextButton').click(function(){
        var currentImage = $('#mainImage').attr('src');
        var nextImage = $('.small-images img[src="'+currentImage+'"]').parent().next().find('img');
        if (nextImage.length > 0) {
            updateMainImageOnClick(nextImage);
        }
    });

});