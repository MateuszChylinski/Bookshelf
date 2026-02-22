function addDeleteFromFavorites(element) {

    const book = {
        id: element.dataset.apiId,
        volumeInfo: {
            title: element.dataset.title,
            authors: [element.dataset.authors],
            pages: element.dataset.pages,
            description: element.dataset.description,
            imageLinks: {
                thumbnail: element.dataset.thumbnailUrl
            }
        }
    };

    const token = document.head.querySelector("[name~=_csrf][content]").content;
    fetch('/favorites', {
        method: 'POST',
        headers: {
            "X-CSRF-TOKEN": token,
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
