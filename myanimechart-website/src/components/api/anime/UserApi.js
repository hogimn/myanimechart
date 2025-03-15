import axios from "axios";

const UserApi = {
  findUser: async () => {
    try {
      const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
      const response = await axios.get(`${gatewayUrl}/query/user/findUser`, {
        withCredentials: true,
      });
      return response.data;
    } catch (error) {
      console.error("Error finding user:", error);
      return false;
    }
  },
  findAllUserAnimeStatuses: async () => {
    try {
      const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
      const response = await axios.get(
        `${gatewayUrl}/query/user/findAllUserAnimeStatuses`,
        {
          withCredentials: true,
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error finding user anime status list:", error);
      return {};
    }
  },
  findUserAnimeStatusById: async (id) => {
    try {
      const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
      const response = await axios.get(
        `${gatewayUrl}/query/user/findUserAnimeStatusById`,
        {
          params: {
            id: id,
          },
          withCredentials: true,
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error finding user anime status:", error);
      return null;
    }
  },
  updateUserAnimeStatus: async (animeListStatus) => {
    if (animeListStatus.status === "Select") {
      animeListStatus.status = null;
    }

    if (animeListStatus.score === 0) {
      animeListStatus.score = null;
    }

    try {
      const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
      const response = await axios.post(
        `${gatewayUrl}/query/user/updateUserAnimeStatus`,
        animeListStatus,
        {
          withCredentials: true,
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error updating user anime status:", error);
      return null;
    } finally {
    }
  },
  deleteUserAnimeStatus: async (animeListStatus) => {
    try {
      const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
      const response = await axios.post(
        `${gatewayUrl}/query/user/deleteUserAnimeStatus`,
        animeListStatus,
        {
          withCredentials: true,
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error deleting user anime status:", error);
      return null;
    } finally {
    }
  },
};

export default UserApi;
