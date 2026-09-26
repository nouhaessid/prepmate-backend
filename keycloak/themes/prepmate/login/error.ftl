<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=false; section>

  <#if section = "form">
    <div class="info-panel info-panel--error">
      <span class="material-icons info-panel__icon">error</span>
      <p class="auth-title info-panel__title">Something went wrong</p>
      <p class="auth-subtitle">${kcSanitize(message.summary)?no_esc}</p>

      <#if client?? && client.baseUrl?has_content>
        <a id="backToApplication" href="${client.baseUrl}" class="pm-link">${kcSanitize(msg("backToApplication"))?no_esc}</a>
      <#else>
        <a href="${url.loginUrl}" class="pm-link">${kcSanitize(msg("backToLogin"))?no_esc}</a>
      </#if>
    </div>
  </#if>
</@layout.registrationLayout>
