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

    document.querySelectorAll("[data-class-filter]").forEach(function (majorSelect) {
        const scope = majorSelect.closest("form") || document;
        const classSelect = scope.querySelector("[data-class-select]");
        if (!classSelect) {
            return;
        }
        const applyFilter = function () {
            const major = majorSelect.value;
            let selectedVisible = false;
            classSelect.querySelectorAll("option").forEach(function (option) {
                const optionMajor = option.getAttribute("data-major");
                const visible = !option.value || !major || optionMajor === major;
                option.hidden = !visible;
                option.disabled = !visible;
                if (visible && option.selected) {
                    selectedVisible = true;
                }
            });
            if (!selectedVisible) {
                classSelect.value = "";
            }
        };
        majorSelect.addEventListener("change", applyFilter);
        applyFilter();
    });
});
