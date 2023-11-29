const recipeId = document.getElementById('recipeId').value

const csrfHeaderName = document.head.querySelector('[name="_csrf_header"]').content;
const csrfHeaderValue = document.head.querySelector('[name="_csrf"]').content;

const commentsCtnr = document.getElementById('commentCtnr')

const commentForm = document.getElementById('commentForm')
commentForm.addEventListener("submit", handleCommentSubmit)

const allComments = []

const displayComments = (comments) => {
    commentsCtnr.innerHTML = comments.map(
        (c)=> {
            return asComment(c)
        }
    ).join('')
}

async function handleCommentSubmit(event){
    event.preventDefault();

    const form = event.currentTarget;
    const url = form.action;
    const formData = new FormData(form);

    try {
        const responseData = await postFormDataAsJson({url, formData});

        commentsCtnr.insertAdjacentHTML("afterbegin", asComment(responseData));

        form.reset();
    } catch (error) {

        let errorObj = JSON.parse(error.message);

        if (errorObj.fieldWithErrors) {
            errorObj.fieldWithErrors.forEach(
                e => {
                    let elementWithError = document.getElementById(e);
                    if (elementWithError) {
                        elementWithError.classList.add("is-invalid");
                    }
                }

            )
        }
    }
    console.log('going to submit a comment!')
}

async function postFormDataAsJson({url, formData}) {

    const plainFormData = Object.fromEntries(formData.entries());
    const formDataAsJSONString = JSON.stringify(plainFormData);

    const fetchOptions = {
        method: "POST",
        headers: {
            [csrfHeaderName] : csrfHeaderValue,
            "Content-Type" : "application/json",
            "Accept" :"application/json"
        },
        body: formDataAsJSONString
    }

    const response = await fetch(url, fetchOptions);

    if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(errorMessage);
    }

    return response.json();
}

function formatDate(dateString) {
    const options = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' };
    return new Date(dateString).toLocaleString('en-US', options).replace(',', '');
}

function deleteComment(commentId){
    fetch(`http://localhost:8080/api/${recipeId}/comments/${commentId}`, {
        method: 'DELETE',
        headers: {
            [csrfHeaderName]: csrfHeaderValue
        }
    })
        .then(response => {
            if (response.ok) {
                // Remove the comment from the UI
                document.getElementById(`commentCntr-${commentId}`).remove();
            }
        })
        .catch(error => console.error('Error:', error));
}


function asComment(comment) {
    let commentHtml = `<div id="commentCntr-${comment.commentId}" class="comment">`

    commentHtml += `<h4 style="text-decoration: underline;">${comment.user}</h4>`
    commentHtml += `<p class="font-italic">${comment.message}</p>`
    commentHtml += `<span>${formatDate(comment.created)}</span>`

    if (comment.canDelete){
        commentHtml += `<button class="btn btn-outline-danger btn-sm ml-5 mb-1" onclick="deleteComment(${comment.commentId})">Delete</button>`
    }

    commentHtml += `</div>`

    return commentHtml
}

fetch(`http://localhost:8080/api/${recipeId}/comments`).
then(response => response.json()).
then(data => {
    for (let comment of data) {
        allComments.push(comment)
    }
    displayComments(allComments)
})