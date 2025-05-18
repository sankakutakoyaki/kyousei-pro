
/******************************************************************************************************* ハンバーガーメニュー作成 */
/**
 * ハンバーガーメニュー作成
 */
const menuOpen = document.querySelector('#menu-open');
const menuClose = document.querySelector('#menu-close');
const menuPanel = document.querySelector('.normal-content');
const mainPanel = document.querySelector('#hamburger-area');

document.addEventListener('DOMContentLoaded', function () {
  if (menuOpen != null) {
    menuOpen.addEventListener('click', () => {
      menuPanel.classList.add('hamburger-open');
      menuPanel.classList.remove('hamburger-close');
      mainPanel.classList.add('dialog');
    });
  };
  if (menuClose != null) {
    menuClose.addEventListener('click', () => {
      menuPanel.classList.add('hamburger-close');
      menuPanel.classList.remove('hamburger-open');
      mainPanel.classList.remove('dialog');
    });
    hamburgerClose();
  }
  if (mainPanel != null) {
    mainPanel.addEventListener('click', () => {
      hamburgerClose();
    });
  };
});

/******************************************************************************************************* ハンバーガーメニューを閉じる（手動） */
/**
 * ハンバーガーメニューを手動で閉じる
 */
function hamburgerClose() {
  const menuClose = document.querySelector('#menu-close');
  menuClose.click();
}