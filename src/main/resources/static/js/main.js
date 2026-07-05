document.addEventListener("submit", function (event) {
    const form = event.target;
    if (form.matches("form[data-confirm]")) {
        const message = form.getAttribute("data-confirm");
        if (!window.confirm(message)) {
            event.preventDefault();
        }
    }
});

document.addEventListener("DOMContentLoaded", function () {
    const path = window.location.pathname;
    document.querySelectorAll("[data-nav]").forEach(function (link) {
        const href = link.getAttribute("href");
        if (href && (path === href || path.startsWith(href + "/"))) {
            link.classList.add("active");
        }
    });
});
