const recipeId = document.getElementById('recipeId').value;

const csrfHeaderName = document.head.querySelector('[name="_csrf_header"]').content;
const csrfHeaderValue = document.head.querySelector('[name="_csrf"]').content;

const commentsCtnr = document.getElementById('commentCtnr');
const commentForm = document.getElementById('commentForm');
const postCommentButton = document.getElementById('postComment');

commentForm.addEventListener('submit', handleCommentSubmit);

async function handleCommentSubmit(event) {
    event.preventDefault();

    const form = event.currentTarget;
    const url = form.action;
    const formData = new FormData(form);

    try {
        const responseData = await postFormDataAsJson({ url, formData });

        commentsCtnr.insertAdjacentHTML('afterbegin', asComment(responseData));

        form.reset();
    } catch (error) {
        handleFormErrors(error);
    }
}

async function postFormDataAsJson({ url, formData }) {
    const plainFormData = Object.fromEntries(formData.entries());
    const formDataAsJSONString = JSON.stringify(plainFormData);

    const fetchOptions = {
        method: 'POST',
        headers: {
            [csrfHeaderName]: csrfHeaderValue,
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        body: formDataAsJSONString,
    };

    const response = await fetch(url, fetchOptions);

    if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(errorMessage);
    }

    return response.json();
}

function handleFormErrors(error) {
    let errorObj = JSON.parse(error.message);

    if (errorObj.fieldWithErrors) {
        errorObj.fieldWithErrors.forEach((e) => {
            let elementWithError = document.getElementById(e);
            if (elementWithError) {
                elementWithError.classList.add('is-invalid');
            }
        });
    }
}

// Add or Remove from Favorites Logic

const favoritesButton = document.getElementById('addOrRemoveButton');
favoritesButton.addEventListener('click', () => addOrRemoveFromFavorites(recipeId));

async function addOrRemoveFromFavorites(recipeId) {
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