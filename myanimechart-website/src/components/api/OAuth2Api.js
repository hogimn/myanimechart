import apiClient, { BASE_URL } from "./apiClient";

const OAuth2Api = {
  startOAuth2Flow: async () => {
    const authorizationUrl = `${BASE_URL}/oauth2/authorize/myanimelist`;
    window.location.href = authorizationUrl;
  },
  isAuthenticated: async () => {
    try {
      const response = await apiClient.get(`/oauth2/status`, {
        withCredentials: true,
      });
      return response.data;
    } catch (error) {
      console.error("Error checking for authentication:", error);
      return false;
    }
  },
  logout: async () => {
    try {
      const response = await apiClient.post(`/oauth2/logout`, null, {
        withCredentials: true,
      });
      return response.data;
    } catch (error) {
      console.error("Error logging out:", error);
      return false;
    }
  },
};

export default OAuth2Api;
