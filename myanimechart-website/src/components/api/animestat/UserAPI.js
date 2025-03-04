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
  getUserAnimeStatusListByYearAndSeason: async (year, season) => {
    try {
      const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
      const response = await axios.get(
        `${gatewayUrl}/query/user/getUserAnimeStatusListByYearAndSeason/${year}/${season}`,
        {
          withCredentials: true,
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error getting user anime status list:", error);
      return {};
    }
  },
  getUserAnimeStatusById: async (id) => {
    try {
      const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
      const response = await axios.get(
        `${gatewayUrl}/query/user/getUserAnimeStatusById`,
        {
          params: {
            id: id,
          },
          withCredentials: true,
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error getting user anime status:", error);
      return null;
    }
  },
  updateUserAnimeStatus: async (animeListStatusDto) => {
    if (animeListStatusDto.status === "Select") {
      animeListStatusDto.status = null;
    }

    if (animeListStatusDto.score === 0) {
      animeListStatusDto.score = null;
    }

    try {
      const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
      const response = await axios.post(
        `${gatewayUrl}/query/user/updateUserAnimeStatus`,
        animeListStatusDto,
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
  deleteUserAnimeStatus: async (animeListStatusDto) => {
    try {
      const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
      const response = await axios.post(
        `${gatewayUrl}/query/user/deleteUserAnimeStatus`,
        animeListStatusDto,
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
