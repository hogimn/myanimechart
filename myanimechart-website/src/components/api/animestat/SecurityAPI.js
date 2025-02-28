import axios from "axios";

const SecurityApi = {
  startOAuth2Flow: async () => {
    const gateway_url = process.env.REACT_APP_GATEWAY_URL;
    const authorizationUrl = `${gateway_url}/security/oauth2/authorize/myanimelist`;
    window.location.href = authorizationUrl;
  },
  isAuthenticated: async () => {
    try {
      const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
      const response = await axios.get(
        `${gatewayUrl}/security/oauth2/isAuthenticated`,
        {
          withCredentials: true,
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error checking for authentication:", error);
      return false;
    }
  },
  logout: async () => {
    try {
      const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
      const response = await axios.post(
        `${gatewayUrl}/security/oauth2/logout`,
        null,
        {
          withCredentials: true,
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error logging out:", error);
      return false;
    }
  },
};

export default SecurityApi;
