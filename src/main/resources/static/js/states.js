let suppressEvent = false;

function setShelfButtonState(isBookOnTheShelf) {
    const shelfButton = document.getElementById("shelfButton")
    shelfButton.dataset.isOnTheShelf = isBookOnTheShelf ? "true" : "false"

    if (isBookOnTheShelf) shelfButton.textContent = "Remove"
    else shelfButton.textContent = "Add"
}

function setIsBookFavoriteState(isBookFavorite) {
    const favoriteView = document.getElementById("favIcon")
    favoriteView.dataset.isFavorite = isBookFavorite ? "true" : "false"

    if (isBookFavorite) {
        favoriteView.classList.add("bi-eye-fill")
        favoriteView.classList.remove("bi-eye")
    } else {
        favoriteView.classList.add("bi-eye")
        favoriteView.classList.remove("bi-eye-fill")
    }
}

function setBookStatusState(bookStatus) {
    const bookStatusView = document.getElementById("bookStateEnum");

    if (bookStatus !== "undefined") {
        bookStatusView.textContent = bookStatus;
    }
}

// reset book rating view (stars rating) when removing book from the shelf
function setBookRatingState() {
    suppressEvent = true;
    coreui.Rating.getInstance("#bookRate").reset()
    suppressEvent = false;
}

function setBookNotesVisibility(isBookOnTheShelf){

    const notesView = document.getElementById("bookNotesForm")
    notesView.classList.toggle("d-none", !isBookOnTheShelf)
    // notesView.style.display = isBookOnTheShelf ? "block" : "none"
    // console.log("ADSADSDASDSDSA "+isBookOnTheShelf)
    // console.log("ADSADSDASDSDSA "+typeof isBookOnTheShelf)
}
// function setShelfButtonState(isBookOnTheShelf) {
//     const shelfButton = document.getElementById("shelfButton")
//     shelfButton.dataset.isOnTheShelf = isBookOnTheShelf ? "true" : "false"
//
//     if (isBookOnTheShelf) shelfButton.textContent = "Remove"
//     else shelfButton.textContent = "Add"
// }