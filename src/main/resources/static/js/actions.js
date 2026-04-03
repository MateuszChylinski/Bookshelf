function addDeleteFromFavorites(element) {

    // get token from book details page
    const token = document.head.querySelector("[name~=_csrf][content]").content;

    // prepare book object
    const book = {
        id: element.dataset.apiId,
        status: element.dataset.status,
        volumeInfo: {
            title: element.dataset.title,
            authors: [element.dataset.authors],
            pageCount: element.dataset.pages,
            description: element.dataset.description,
            categories: [element.dataset.genres],
            imageLinks: {
                thumbnail: element.dataset.thumbnailUrl
            }
        }
    };

    // check if user has chosen status for the book
    if (!element.dataset.status) {
        alert("Before adding book to your favorites, be sure to pick a book status!")
        return;
    }

    // book already in favorites
    if (element.classList.contains("bi-heart-fill")) {
        const bookId = element.dataset.apiId;
        fetch(`/favorites/${bookId}`, {
            method: 'DELETE',
            headers: {
                'X-CSRF-TOKEN': token,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(book)
        })
            .then(response => {
                    if (response.ok) {
                        element.classList.toggle("bi-heart-fill")
                        element.classList.toggle("bi-heart")
                    }
                }
            );
    }
    // book is not in favorites
    else {
        fetch('/favorites', {
            method: 'POST',
            headers: {
                'X-CSRF-TOKEN': token,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(book)
        })
            .then(response => {
                if (response.ok) {
                    element.classList.toggle("bi-heart");
                    element.classList.toggle("bi-heart-fill");
                }
            });
    }
}

function test(element) {
    document.getElementById("heartIcon").dataset.status = element.dataset.status;
}