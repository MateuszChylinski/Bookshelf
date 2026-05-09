function prepareBook() {
    const view = document.getElementById("heartIcon");

    return {
        id: view.dataset.apiId,
        status: view.dataset.status,
        volumeInfo: {
            title: view.dataset.title,
            authors: [view.dataset.authors],
            pageCount: view.dataset.pages,
            description: view.dataset.description,
            categories: [view.dataset.genres],
            imageLinks: {
                thumbnail: view.dataset.thumbnailUrl
            }
        }
    };
}

