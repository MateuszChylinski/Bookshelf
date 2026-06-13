function addDeleteFromFavorites(element) {

    // get token from book details page
    const token = document.head.querySelector("[name~=_csrf][content]").content;

// book already in favorites
    if (element.classList.contains("bi-eye-fill")) {
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
                        element.classList.toggle("bi-eye-fill")
                        element.classList.toggle("bi-eye")
                    }
                }
            );
    }

// book is not in favorites
    else {
        let params = [];

        if (window.bookRate !== null) params.push(`rating=${window.bookRate}`);
        if (window.bookStatus !== 'undefined') params.push(`status=${window.bookStatus}`);
        if (window.isFavorite !== 'undefined') params.push(`isFavorite=${window.bookIsFavorite}`)


        const queryString = params.length > 0 ? `?${params.join('&')}` : '';

        fetch(`/favorites${queryString}`, {
            method: 'POST',
            headers: {
                'X-CSRF-TOKEN': token,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(prepareBook())
        })
            .then(response => {
                if (response.ok) {
                    element.classList.toggle("bi-eye");
                    element.classList.toggle("bi-eye-fill");
                }
            });
    }
}

function updateShelf(field) {
    const csrfToken = document.head.querySelector("[name~=_csrf][content]").content

    let params = [];
    if (field.bookRate) params.push(`rating=${field.bookRate}`);
    if (field.bookStatus) params.push(`status=${field.bookStatus}`)
    if (field.isFavorite !== undefined) params.push(`isFavorite=${field.isFavorite}`)
    const queryString = params.length > 0 ? `?${params.join('&')}` : '';

    console.log(queryString)
    console.log(JSON.stringify(prepareBook()))

    fetch(`/favorites${queryString}`, {
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': csrfToken,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(prepareBook())
    })
        .then(response => {
            if (response.ok) {
                console.log("success")
            }
        });
}
