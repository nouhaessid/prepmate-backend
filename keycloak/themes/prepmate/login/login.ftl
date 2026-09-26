<#import "template.ftl" as layout>
<@layout.registrationLayout
  displayInfo=realm.password && realm.registrationAllowed && !registrationDisabled??
  displayMessage=!messagesPerField.existsError('username','password'); section>

  <#if section = "form">
    <h1 class="auth-title">Log in to your account</h1>
    <p class="auth-subtitle">Pick up where you left off.</p>

    <form id="kc-form-login" class="auth-form" onsubmit="login.disabled = true; return true;" action="${url.loginAction}" method="post">

      <#if !usernameHidden??>
        <div class="pm-field">
          <label for="username" class="pm-label">
            <#if !realm.loginWithEmailAllowed>${msg("username")}<#elseif !realm.registrationEmailAsUsername>${msg("usernameOrEmail")}<#else>${msg("email")}</#if>
          </label>
          <input
            tabindex="1"
            id="username"
            class="pm-input <#if messagesPerField.existsError('username','password')>pm-input-error</#if>"
            name="username"
            value="${(login.username!'')}"
            type="text"
            autocomplete="username"
            aria-invalid="<#if messagesPerField.existsError('username','password')>true<#else>false</#if>"
            data-required="true"
            data-required-msg="<#if !realm.loginWithEmailAllowed>Username is required<#elseif !realm.registrationEmailAsUsername>Username or email is required<#else>Email is required</#if>"
            <#if realm.loginWithEmailAllowed && realm.registrationEmailAsUsername>data-email="true" data-email-msg="Enter a valid email address"</#if>
          />
          <#if messagesPerField.existsError('username','password')>
            <span class="pm-error" id="input-error" aria-live="polite">
              ${kcSanitize(messagesPerField.getFirstError('username','password'))?no_esc}
            </span>
          </#if>
        </div>
      </#if>

      <div class="pm-field">
        <label for="password" class="pm-label">${msg("password")}</label>
        <div class="pm-input-wrap">
          <input
            tabindex="2"
            id="password"
            class="pm-input <#if messagesPerField.existsError('username','password')>pm-input-error</#if>"
            name="password"
            type="password"
            autocomplete="current-password"
            aria-invalid="<#if messagesPerField.existsError('username','password')>true<#else>false</#if>"
            data-required="true"
            data-required-msg="Password is required"
          />
          <button type="button" class="pm-toggle-visibility" data-toggle-for="password" aria-label="Show password">
            <span class="material-icons">visibility</span>
          </button>
        </div>
      </div>

      <div class="pm-row-between">
        <#if realm.rememberMe && !usernameHidden??>
          <label class="pm-checkbox">
            <input tabindex="3" id="rememberMe" name="rememberMe" type="checkbox" <#if login.rememberMe??>checked</#if> />
            <span>${msg("rememberMe")}</span>
          </label>
        <#else>
          <span></span>
        </#if>

        <#if realm.resetPasswordAllowed>
          <a tabindex="5" href="${url.loginResetCredentialsUrl}" class="pm-link">${msg("doForgotPassword")}</a>
        </#if>
      </div>

      <input type="hidden" id="id-hidden-input" name="credentialId" <#if auth.selectedCredential?has_content>value="${auth.selectedCredential}"</#if>/>
      <button tabindex="4" class="pm-button-primary" name="login" id="kc-login" type="submit">
        ${msg("doLogIn")}
      </button>
    </form>

  <#elseif section = "info">
    <#if realm.password && realm.registrationAllowed && !registrationDisabled??>
      <p class="auth-footnote-text">
        ${msg("noAccount")}
        <a tabindex="6" href="${url.registrationUrl}" class="pm-link">${msg("doRegister")}</a>
      </p>
    </#if>
  </#if>
</@layout.registrationLayout>
