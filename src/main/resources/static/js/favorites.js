const favoritesButton = document.getElementById('addOrRemoveButton');
favoritesButton.addEventListener('click', handleFavoritesButtonClick);

// Function to handle the click event on the "Add or Remove from Favorites" button
async function handleFavoritesButtonClick(event) {
    event.preventDefault(); // Prevent the default button click behavior

    const fetchOptions = {
        method: 'POST',
        headers: {
            [csrfHeaderName]: csrfHeaderValue,
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
    };

    try {
        const response = await fetch(`/api/recipes/${recipeId}/addOrRemoveFromFavorites`, fetchOptions);

        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(errorMessage);
        }

        const isFavorite = await response.json();

        // Update button text and icon based on the new status
        const buttonText = isFavorite ? 'Remove from Favorites' : 'Add to Favorites';
        const iconClass = isFavorite ? 'fas fa-heart-circle-minus' : 'fas fa-heart-circle-plus';

        // Update the icon class and text
        document.getElementById('favoriteIcon').className = iconClass;
        document.getElementById('favoriteText').innerText = buttonText;

    } catch (error) {
        console.error('Error:', error);
    }
}