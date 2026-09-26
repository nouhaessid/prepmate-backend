<#import "template.ftl" as layout>
<@layout.registrationLayout
  displayInfo=true
  displayMessage=!messagesPerField.existsError('firstName','lastName','email','username','password','password-confirm'); section>

  <#if section = "form">
    <h1 class="auth-title">Create your account</h1>
    <p class="auth-subtitle">Start practicing in a couple of minutes.</p>

    <form id="kc-register-form" class="auth-form" action="${url.registrationAction}" method="post">

      <div class="pm-field-row">
        <div class="pm-field">
          <label for="firstName" class="pm-label">${msg("firstName")}</label>
          <input
            type="text"
            id="firstName"
            class="pm-input <#if messagesPerField.existsError('firstName')>pm-input-error</#if>"
            name="firstName"
            value="${(register.formData.firstName!'')}"
            autocomplete="given-name"
            data-required="true"
            data-required-msg="First name is required"
          />
          <#if messagesPerField.existsError('firstName')>
            <span class="pm-error">${kcSanitize(messagesPerField.get('firstName'))?no_esc}</span>
          </#if>
        </div>

        <div class="pm-field">
          <label for="lastName" class="pm-label">${msg("lastName")}</label>
          <input
            type="text"
            id="lastName"
            class="pm-input <#if messagesPerField.existsError('lastName')>pm-input-error</#if>"
            name="lastName"
            value="${(register.formData.lastName!'')}"
            autocomplete="family-name"
            data-required="true"
            data-required-msg="Last name is required"
          />
          <#if messagesPerField.existsError('lastName')>
            <span class="pm-error">${kcSanitize(messagesPerField.get('lastName'))?no_esc}</span>
          </#if>
        </div>
      </div>

      <div class="pm-field">
        <label for="email" class="pm-label">${msg("email")}</label>
        <input
          type="text"
          id="email"
          class="pm-input <#if messagesPerField.existsError('email')>pm-input-error</#if>"
          name="email"
          value="${(register.formData.email!'')}"
          autocomplete="email"
          data-required="true"
          data-required-msg="Email is required"
          data-email="true"
          data-email-msg="Enter a valid email address"
        />
        <#if messagesPerField.existsError('email')>
          <span class="pm-error">${kcSanitize(messagesPerField.get('email'))?no_esc}</span>
        </#if>
      </div>

      <#if !realm.registrationEmailAsUsername>
        <div class="pm-field">
          <label for="username" class="pm-label">${msg("username")}</label>
          <input
            type="text"
            id="username"
            class="pm-input <#if messagesPerField.existsError('username')>pm-input-error</#if>"
            name="username"
            value="${(register.formData.username!'')}"
            autocomplete="username"
            data-required="true"
            data-required-msg="Username is required"
          />
          <#if messagesPerField.existsError('username')>
            <span class="pm-error">${kcSanitize(messagesPerField.get('username'))?no_esc}</span>
          </#if>
        </div>
      </#if>

      <#if passwordRequired??>
        <div class="pm-field">
          <label for="password" class="pm-label">${msg("password")}</label>
          <div class="pm-input-wrap">
            <input
              type="password"
              id="password"
              class="pm-input <#if messagesPerField.existsError('password','password-confirm')>pm-input-error</#if>"
              name="password"
              autocomplete="new-password"
              data-required="true"
              data-required-msg="Password is required"
              data-minlength="8"
              data-minlength-msg="Use at least 8 characters"
            />
            <button type="button" class="pm-toggle-visibility" data-toggle-for="password" aria-label="Show password">
              <span class="material-icons">visibility</span>
            </button>
          </div>
          <#if messagesPerField.existsError('password')>
            <span class="pm-error">${kcSanitize(messagesPerField.get('password'))?no_esc}</span>
          </#if>
        </div>

        <div class="pm-field">
          <label for="password-confirm" class="pm-label">${msg("passwordConfirm")}</label>
          <input
            type="password"
            id="password-confirm"
            class="pm-input <#if messagesPerField.existsError('password-confirm')>pm-input-error</#if>"
            name="password-confirm"
            autocomplete="new-password"
            data-required="true"
            data-required-msg="Please confirm your password"
            data-match="password"
            data-match-msg="Passwords don't match"
          />
          <#if messagesPerField.existsError('password-confirm')>
            <span class="pm-error">${kcSanitize(messagesPerField.get('password-confirm'))?no_esc}</span>
          </#if>
        </div>
      </#if>

      <button class="pm-button-primary" type="submit">${msg("doRegister")}</button>
    </form>

  <#elseif section = "info">
    <p class="auth-footnote-text">
      Already have an account?
      <a href="${url.loginUrl}" class="pm-link">${kcSanitize(msg("doLogIn"))?no_esc}</a>
    </p>
  </#if>
</@layout.registrationLayout>
