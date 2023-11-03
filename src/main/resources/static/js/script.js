$(document).ready(function(){
    // Function to change the main image
    function changeMainImage(src) {
        $('#mainImage').attr('src', src);
    }

    // Set the initial image to the first small image
    var initialImage = $('.small-images img:first').attr('src');
    changeMainImage(initialImage);

    // Clicking on small images should change the main image
    $('.small-images img').click(function(){
        var src = $(this).attr('src');
        changeMainImage(src);
    });

    // Manual navigation buttons
    $('#prevButton').click(function(){
        var currentImage = $('#mainImage').attr('src');
        var prevImage = $('.small-images img[src="'+currentImage+'"]').prev().attr('src');
        if (prevImage) {
            changeMainImage(prevImage);
        }
    });

    $('#nextButton').click(function(){
        var currentImage = $('#mainImage').attr('src');
        var nextImage = $('.small-images img[src="'+currentImage+'"]').next().attr('src');
        if (nextImage) {
            changeMainImage(nextImage);
        }
    });
});