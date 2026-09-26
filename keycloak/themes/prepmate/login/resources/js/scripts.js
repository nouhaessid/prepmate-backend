// PrepMate Keycloak theme — client-side behavior.
//
// Two independent pieces:
// 1. Password show/hide toggle (unchanged from before).
// 2. Field validation, driven entirely by data-* attributes so the same
//    engine works on login, register, forgot-password, and set-new-password
//    without four separate copies:
//      data-required                → non-empty on blur / submit
//      data-required-msg="..."      → message shown when empty
//      data-email                   → must look like an email, if non-empty
//      data-email-msg="..."         → message shown when malformed
//      data-minlength="8"           → minimum length, if non-empty
//      data-minlength-msg="..."     → message shown when too short
//      data-match="otherFieldId"    → must equal the value of #otherFieldId
//      data-match-msg="..."         → message shown when they differ
//
// This is a UX layer only. Keycloak's own server-side validation is still
// the source of truth — if JavaScript is disabled, the form posts as
// before and Keycloak's normal error rendering still works.

document.addEventListener('DOMContentLoaded', function () {
    initPasswordToggles();
    initFormValidation();
});

function initPasswordToggles() {
    document.querySelectorAll('.pm-toggle-visibility').forEach(function (button) {
        button.addEventListener('click', function () {
            var targetId = button.getAttribute('data-toggle-for');
            var input = document.getElementById(targetId);
            if (!input) return;

            var icon = button.querySelector('.material-icons');
            var isPassword = input.getAttribute('type') === 'password';

            input.setAttribute('type', isPassword ? 'text' : 'password');
            button.setAttribute('aria-label', isPassword ? 'Hide password' : 'Show password');
            if (icon) {
                icon.textContent = isPassword ? 'visibility_off' : 'visibility';
            }
        });
    });
}

function initFormValidation() {
    var EMAIL_PATTERN = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    document.querySelectorAll('.auth-form').forEach(function (form) {
        var fields = form.querySelectorAll('[data-required], [data-email], [data-minlength], [data-match]');
        if (fields.length === 0) return;

        fields.forEach(function (input) {
            // "touch a field and then touch outside it" == blur
            input.addEventListener('blur', function () {
                validateField(input);
            });

            // Once a field has shown an error, clear it live as the user fixes it
            // rather than making them blur again to find out it's now valid.
            input.addEventListener('input', function () {
                if (input.classList.contains('pm-input-error')) {
                    validateField(input);
                }
            });
        });

        form.addEventListener('submit', function (event) {
            var firstInvalid = null;

            fields.forEach(function (input) {
                var isValid = validateField(input);
                if (!isValid && !firstInvalid) {
                    firstInvalid = input;
                }
            });

            if (firstInvalid) {
                event.preventDefault();
                firstInvalid.focus();

                var container = firstInvalid.closest('.pm-field');
                if (container) {
                    container.classList.remove('pm-shake');
                    // restart the animation even if it was just played
                    void container.offsetWidth;
                    container.classList.add('pm-shake');
                }
            }
        });
    });

    function validateField(input) {
        var value = input.value.trim();

        if (input.hasAttribute('data-required') && value.length === 0) {
            showFieldError(input, input.getAttribute('data-required-msg') || 'This field is required');
            return false;
        }

        if (input.hasAttribute('data-email') && value.length > 0 && !EMAIL_PATTERN.test(value)) {
            showFieldError(input, input.getAttribute('data-email-msg') || 'Enter a valid email address');
            return false;
        }

        if (input.hasAttribute('data-minlength') && value.length > 0) {
            var min = parseInt(input.getAttribute('data-minlength'), 10);
            if (value.length < min) {
                showFieldError(input, input.getAttribute('data-minlength-msg') || ('Use at least ' + min + ' characters'));
                return false;
            }
        }

        if (input.hasAttribute('data-match') && value.length > 0) {
            var other = document.getElementById(input.getAttribute('data-match'));
            if (other && other.value !== value) {
                showFieldError(input, input.getAttribute('data-match-msg') || "Passwords don't match");
                return false;
            }
        }

        clearFieldError(input);

        // If another field is validated against this one (e.g. confirm-password
        // depends on password), re-check it live as this one changes.
        document.querySelectorAll('[data-match="' + input.id + '"]').forEach(function (dependent) {
            if (dependent.value.length > 0) {
                validateField(dependent);
            }
        });

        return true;
    }

    function showFieldError(input, message) {
        var container = input.closest('.pm-field');
        input.classList.add('pm-input-error');
        input.setAttribute('aria-invalid', 'true');
        if (!container) return;

        var error = container.querySelector('.pm-error-client');
        if (!error) {
            error = document.createElement('span');
            error.className = 'pm-error pm-error-client';
            error.id = input.id + '-client-error';
            container.appendChild(error);
        }
        error.textContent = message;
        input.setAttribute('aria-describedby', error.id);
    }

    function clearFieldError(input) {
        var container = input.closest('.pm-field');
        input.classList.remove('pm-input-error');
        input.setAttribute('aria-invalid', 'false');
        if (!container) return;

        var error = container.querySelector('.pm-error-client');
        if (error) error.remove();
    }
}