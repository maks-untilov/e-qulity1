const openSideBarBtn = document.querySelector('.sidebar__open')
const closeSideBarBtn = document.querySelector('.sidebar__close')
const sidebar = document.querySelector('.sidebar')
const editButtons = [...document.querySelectorAll('.table__item--edit')]
const popup = document.querySelector('.popup')
const app = document.querySelector('#app')
const closePopupBtn = document.querySelector('.popup__close-icon')
const popupContent = document.querySelector('.popup__content')

openSideBarBtn.addEventListener('click', () => {
  sidebar.classList.add('show-sidebar')
})

closeSideBarBtn.addEventListener('click', () => {
  sidebar.classList.remove('show-sidebar')
})

editButtons.forEach((editButton) => {
  editButton.addEventListener('click', () => {
    popup.classList.add('back-blur')
    popup.classList.add('popup--active')

    popupContent.style.opacity = '1'
    popupContent.classList.add('expanded')

    app.style.overflowY = 'hidden'
    document.body.style.overflowY = 'hidden'
  })
})

closePopupBtn.addEventListener('click', () => {
  closePopup()
})

function closePopup() {
  popupContent.style.opacity = '0'
  popup.classList.remove('back-blur')

  setTimeout(() => {
    popup.classList.remove('popup--active')
    app.style.overflowY = 'auto'
    document.body.style.overflowY = 'auto'
  }, 100 * 2)

  document.removeEventListener('keyup', onKeyPress)
}
