import axios from "axios";

const UserApi = {
  getUser: async () => {
    try {
      const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
      const response = await axios.get(`${gatewayUrl}/query/user/getUser`, {
        withCredentials: true,
      });
      return response.data;
    } catch (error) {
      console.error("Error getting user:", error);
      return false;
    }
  },
};

export default UserApi;
