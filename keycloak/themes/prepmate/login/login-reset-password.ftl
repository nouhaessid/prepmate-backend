<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=true displayInfo=true; section>

  <#if section = "form">
    <h1 class="auth-title">Reset your password</h1>
    <p class="auth-subtitle">Enter your username or email and we'll send you instructions to reset it.</p>

    <form id="kc-reset-password-form" class="auth-form" action="${url.loginAction}" method="post">
      <div class="pm-field">
        <label for="username" class="pm-label">
          <#if !realm.loginWithEmailAllowed>${msg("username")}<#elseif !realm.registrationEmailAsUsername>${msg("usernameOrEmail")}<#else>${msg("email")}</#if>
        </label>
        <input
          type="text"
          id="username"
          class="pm-input <#if messagesPerField.existsError('username')>pm-input-error</#if>"
          name="username"
          autofocus
          value="${(auth.attemptedUsername)!''}"
          aria-invalid="<#if messagesPerField.existsError('username')>true<#else>false</#if>"
          data-required="true"
          data-required-msg="<#if !realm.loginWithEmailAllowed>Username is required<#elseif !realm.registrationEmailAsUsername>Username or email is required<#else>Email is required</#if>"
          <#if realm.loginWithEmailAllowed && realm.registrationEmailAsUsername>data-email="true" data-email-msg="Enter a valid email address"</#if>
        />
        <#if messagesPerField.existsError('username')>
          <span class="pm-error">${kcSanitize(messagesPerField.get('username'))?no_esc}</span>
        </#if>
      </div>

      <button class="pm-button-primary" type="submit">${msg("doSubmit")}</button>
    </form>

  <#elseif section = "info">
    <p class="auth-footnote-text">
      <a href="${url.loginUrl}" class="pm-link">${kcSanitize(msg("backToLogin"))?no_esc}</a>
    </p>
  </#if>
</@layout.registrationLayout>
