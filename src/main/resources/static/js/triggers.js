// Book status (enum)
const statusElement = document.querySelectorAll(".dropdown-item")
statusElement.forEach(element => {
    element.addEventListener("click", event => {
        updateShelf({bookStatus: element.dataset.status});
    })
})

// Book rating (stars)
const rateElement = document.querySelector(".bookRate")
rateElement.addEventListener('change.coreui.rating', listener =>{
    updateShelf({bookRate: listener.value})
})

// Book favorite (heart icon)
const iconElement = document.querySelector(".bi-eye")
iconElement.addEventListener('click', event => {
    if (iconElement.classList.contains("bi-eye")) {

        iconElement.classList.toggle("bi-eye")
        iconElement.classList.toggle("bi-eye-fill")
        updateShelf({isFavorite: true})
    } else {
        iconElement.classList.toggle("bi-eye-fill")
        iconElement.classList.toggle("bi-eye")
        updateShelf({isFavorite: false})
    }
})
