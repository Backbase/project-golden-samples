terraform {
  required_providers {
    keycloak = {
      source  = "mrparkers/keycloak"
      version = "4.0.1"
    }
  }
}

provider "keycloak" {
  client_id = "admin-cli"
  username  = "admin"
  password  = "admin"
  url       = "http://kubernetes.docker.internal"
  base_path = "/auth"
}

resource "keycloak_realm" "backbase" {
  realm             = "backbase"
  enabled           = true
  display_name      = "Backbase"
  display_name_html = "<div class=\"kc-logo-text\"><span>Backbase</span></div>"
}

resource "keycloak_role" "users" {
  realm_id = keycloak_realm.backbase.id
  name     = "USER_ROLE"
}

resource "keycloak_user" "provisioner" {
  realm_id = keycloak_realm.backbase.id
  username = "provisioner"
  email    = "test-user@fakedomain.com"
  initial_password {
    value = "provisioner"
  }
  attributes = {
    "mobileNumber" = "1234567890"
  }
}

resource "keycloak_user_roles" "user_roles" {
  realm_id = keycloak_realm.backbase.id
  user_id  = keycloak_user.provisioner.id

  role_ids = [
    keycloak_role.users.id
  ]
}

resource "keycloak_openid_client" "tooling_client" {
  client_id                    = "bb-tooling-client"
  realm_id                     = keycloak_realm.backbase.id
  access_type                  = "PUBLIC"
  direct_access_grants_enabled = true
  web_origins                  = [
    "*",
  ]
}

resource "keycloak_generic_protocol_mapper" "realm_authorities_attribute_mapper" {
  realm_id        = keycloak_realm.backbase.id
  client_id       = keycloak_openid_client.tooling_client.id
  name            = "realm-access"
  protocol        = "openid-connect"
  protocol_mapper = "oidc-usermodel-realm-role-mapper"
  config          = {
    "multivalued"          = "true"
    "userinfo.token.claim" = "true"
    "id.token.claim"       = "false"
    "access.token.claim"   = "true"
    "claim.name"           = "authorities"
    "jsonType.label"       = "String"
  }
}

resource "keycloak_authentication_flow" "web_browser_flow" {
  alias    = "Web Browser Flow"
  realm_id = keycloak_realm.backbase.id
}

resource "keycloak_authentication_execution" "web_browser_flow_execution_one" {
  realm_id          = keycloak_realm.backbase.id
  parent_flow_alias = keycloak_authentication_flow.web_browser_flow.alias
  authenticator     = "auth-cookie"
  requirement       = "ALTERNATIVE"
}

resource "keycloak_authentication_execution" "web_browser_flow_execution_two" {
  realm_id          = keycloak_realm.backbase.id
  parent_flow_alias = keycloak_authentication_flow.web_browser_flow.alias
  authenticator     = "identity-provider-redirector"
  requirement       = "ALTERNATIVE"

  depends_on = [
    keycloak_authentication_execution.web_browser_flow_execution_one
  ]
}
