<#macro registrationLayout bodyClass="" displayInfo=false displayMessage=true displayRequiredFields=false showAnotherWayIfPresent=true>
<!DOCTYPE html>
<html lang="${(locale.currentLanguageTag)!'en'}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${msg("loginTitle",(realm.displayName!'PrepMate'))}</title>
    <link rel="icon" type="image/svg+xml" href="${url.resourcesPath}/img/favicon.svg">

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=Space+Grotesk:wght@500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">

    <link rel="stylesheet" href="${url.resourcesPath}/css/styles.css">
</head>
<body class="${bodyClass}">
  <div class="auth-page">
    <div class="auth-card">
      <a href="#" class="brand-row">
        <span class="logo-mark" aria-hidden="true">&lt;/&gt;</span>
        <span class="brand-name">PrepMate</span>
      </a>

      <#-- Top-level message banner (errors, warnings, info, success) -->
      <#if displayMessage && message?has_content>
        <div class="kc-alert kc-alert-${message.type}">
          <#if message.type = 'success'><span class="material-icons">check_circle</span></#if>
          <#if message.type = 'warning'><span class="material-icons">warning</span></#if>
          <#if message.type = 'error'><span class="material-icons">error</span></#if>
          <#if message.type = 'info'><span class="material-icons">info</span></#if>
          <span>${kcSanitize(message.summary)?no_esc}</span>
        </div>
      </#if>

      <#nested "form">

      <#if displayInfo>
        <div class="auth-footnote">
          <#nested "info">
        </div>
      </#if>
    </div>
  </div>

  <script src="${url.resourcesPath}/js/scripts.js" type="text/javascript"></script>
</body>
</html>
</#macro>
