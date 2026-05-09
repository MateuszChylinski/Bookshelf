window.bookStatus = null;
const statusElement = document.querySelectorAll(".dropdown-item")
statusElement.forEach(element => {
    element.addEventListener("click", event => {
        window.bookStatus = element.dataset.status
    })
})

window.bookRate = null;
const rateElement = document.querySelector(".bookRate")
rateElement.addEventListener('change.coreui.rating', bookListener => {
    window.bookRate = bookListener.value
})

