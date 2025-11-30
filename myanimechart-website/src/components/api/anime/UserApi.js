import axios from "axios";

const UserApi = {
  getCurrentUser: async () => {
    try {
      const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
      const response = await axios.get(`${gatewayUrl}/application/user`, {
        withCredentials: true,
      });
      return response.data;
    } catch (error) {
      console.error("Error finding user:", error);
      return false;
    }
  },
  findAnimeStatuses: async () => {
    try {
      const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
      const response = await axios.get(
        `${gatewayUrl}/application/user/anime-statuses`,
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
  findAnimeStatus: async (id) => {
    try {
      const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
      const response = await axios.get(
        `${gatewayUrl}/application/user/anime-status`,
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
  updateAnimeStatus: async (animeListStatus) => {
    if (animeListStatus.status === "Select") {
      animeListStatus.status = null;
    }

    if (animeListStatus.score === 0) {
      animeListStatus.score = null;
    }

    try {
      const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
      const response = await axios.post(
        `${gatewayUrl}/application/user/anime-status/update`,
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
  deleteAnimeStatus: async (animeListStatus) => {
    try {
      const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
      const response = await axios.post(
        `${gatewayUrl}/application/user/anime-status/delete`,
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
