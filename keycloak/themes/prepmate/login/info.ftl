<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=false; section>

  <#if section = "form">
    <div class="info-panel">
      <span class="material-icons info-panel__icon">mark_email_read</span>
      <p class="auth-title info-panel__title">Check your inbox</p>
      <p class="auth-subtitle">
        ${message.summary}
        <#if requiredActions??>
          <#list requiredActions>: <b><#items as reqActionItem>${msg("requiredAction.${reqActionItem}")}<#sep>, </#items></b></#list>
        </#if>
      </p>

      <#if !skipLink??>
        <#if pageRedirectUri?has_content>
          <a href="${pageRedirectUri}" class="pm-link">${kcSanitize(msg("backToApplication"))?no_esc}</a>
        <#elseif actionUri?has_content>
          <a href="${actionUri}" class="pm-link">${kcSanitize(msg("proceedWithAction"))?no_esc}</a>
        <#elseif (client.baseUrl)?has_content>
          <a href="${client.baseUrl}" class="pm-link">${kcSanitize(msg("backToApplication"))?no_esc}</a>
        <#else>
          <a href="${url.loginUrl}" class="pm-link">${kcSanitize(msg("backToLogin"))?no_esc}</a>
        </#if>
      </#if>
    </div>
  </#if>
</@layout.registrationLayout>
