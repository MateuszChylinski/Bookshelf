// Book rating (stars)
const rateElement = document.querySelector(".bookRate")
rateElement.addEventListener('change.coreui.rating', listener => {

    if (suppressEvent) return;

    addOnTheShelfOrDescribe({bookRate: listener.value})
    setShelfButtonState(true)
})


const favoriteIcon = document.getElementById("favIcon");
favoriteIcon.addEventListener('click', event => {
    const favoriteCurrentState = favoriteIcon.dataset.isFavorite === "true";
    const nextState = !favoriteCurrentState;

    setIsBookFavoriteState(nextState);
    setShelfButtonState(true)

    addOnTheShelfOrDescribe({isFavorite: nextState})
})

const shelfButton = document.getElementById("shelfButton")
shelfButton.addEventListener("click", () => {

    const flag = shelfButton.dataset.isOnTheShelf === "true";
    const nextFlagState = !flag;
    const bookApiId = shelfButton.dataset.bookApiId;

    setShelfButtonState(nextFlagState)


    if (nextFlagState) { // book is on the shelf
        addOnTheShelfOrDescribe({})
    } else { // book is not on the shelf
        removeFromTheShelf(bookApiId)
    }
})

// Book status (enum)
const statusElement = document.querySelectorAll(".dropdown-item")
statusElement.forEach(element => {
    element.addEventListener("click", event => {

        setBookStatusState(element.dataset.status)
        setShelfButtonState(true)

        addOnTheShelfOrDescribe({bookStatus: element.dataset.status})
    })
});
