const recipeId = document.getElementById('recipeId').value

const csrfHeaderName = document.head.querySelector('[name="_csrf_header"]').content;
const csrfHeaderValue = document.head.querySelector('[name="_csrf"]').content;

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

        // Update button text and style based on the new status
        favoritesButton.innerText = isFavorite ? 'Remove from Favorites' : 'Add to Favorites';

    } catch (error) {
        console.error('Error:', error);
    }
}