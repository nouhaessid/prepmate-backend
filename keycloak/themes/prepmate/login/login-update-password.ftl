<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=true; section>

  <#if section = "form">
    <h1 class="auth-title">Choose a new password</h1>
    <p class="auth-subtitle">Make it something you haven't used here before.</p>

    <form id="kc-passwd-update-form" class="auth-form" action="${url.loginAction}" method="post">
      <div class="pm-field">
        <label for="password-new" class="pm-label">${msg("passwordNew")}</label>
        <div class="pm-input-wrap">
          <input
            type="password"
            id="password-new"
            class="pm-input <#if messagesPerField.existsError('password')>pm-input-error</#if>"
            name="password-new"
            autofocus
            autocomplete="new-password"
            data-required="true"
            data-required-msg="Password is required"
            data-minlength="8"
            data-minlength-msg="Use at least 8 characters"
          />
          <button type="button" class="pm-toggle-visibility" data-toggle-for="password-new" aria-label="Show password">
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
          data-match="password-new"
          data-match-msg="Passwords don't match"
        />
        <#if messagesPerField.existsError('password-confirm')>
          <span class="pm-error">${kcSanitize(messagesPerField.get('password-confirm'))?no_esc}</span>
        </#if>
      </div>

      <#if isAppInitiatedAction??>
        <div class="pm-buttons-row">
          <button class="pm-button-secondary" type="submit" name="cancel-aia" value="true">${msg("doCancel")}</button>
          <button class="pm-button-primary" type="submit">${msg("doSubmit")}</button>
        </div>
      <#else>
        <button class="pm-button-primary" type="submit">${msg("doSubmit")}</button>
      </#if>
    </form>
  </#if>
</@layout.registrationLayout>
