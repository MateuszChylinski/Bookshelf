function removeFromTheShelf(bookApiId) {
    const token = document.head.querySelector("[name~=_csrf][content]").content

    if (bookApiId !== undefined) {
        fetch(`/favorites/${bookApiId}`, {
            method: 'DELETE',
            headers: {
                'X-CSRF-TOKEN': token,
            }
        })
            .then(response => {
                if (response.ok) {
                    setIsBookFavoriteState(false);
                    setBookStatusState("Book Status");
                    setBookRatingState();
                    setBookNotesVisibility(false)
                } else {
                    //TODO flag error response
                }
            })
    }
}

function addOnTheShelfOrDescribe(field) {
    const token = document.head.querySelector("[name~=_csrf][content]").content

    // check for selected fields
    let params = [];

    if (field.bookRate !== undefined) params.push(`rating=${field.bookRate}`);
    if (field.bookStatus !== undefined) params.push(`status=${field.bookStatus}`)
    if (field.isFavorite !== undefined) params.push(`isFavorite=${field.isFavorite}`)

    // prepare a full query string
    const query = params.length > 0 ? `?${params.join('&')}` : '';

    // prepare a fetch
    fetch(`/favorites${query}`, {
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': token,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(prepareBook())
    })
        .then(response => {
            if (response.ok) {
                console.log(response)
                setBookNotesVisibility(true)

            } else {
                // TODO flag error response.
                console.log("error")
                console.log(response)
            }
        });
}