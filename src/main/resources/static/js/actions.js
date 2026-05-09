function addDeleteFromFavorites(element) {

    // get token from book details page
    const token = document.head.querySelector("[name~=_csrf][content]").content;

    // book already in favorites
    if (element.classList.contains("bi-heart-fill")) {
        const bookId = element.dataset.apiId;
        fetch(`/favorites/${bookId}`, {
            method: 'DELETE',
            headers: {
                'X-CSRF-TOKEN': token,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(prepareBook())
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
        fetch(`/favorites?rating=${window.bookRate}&status=${window.bookStatus}`, {
            method: 'POST',
            headers: {
                'X-CSRF-TOKEN': token,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(prepareBook())
        })
            .then(response => {
                if (response.ok) {
                    element.classList.toggle("bi-heart");
                    element.classList.toggle("bi-heart-fill");
                }
            });
    }
}