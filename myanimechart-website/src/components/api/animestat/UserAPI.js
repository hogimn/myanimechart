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
  getUserAnimeListByYearAndSeason: async (year, season) => {
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
      console.error("Error getting user anime list:", error);
      return false;
    }
  },
  getUserAnimeById: async (id) => {
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
      console.error("Error getting user anime:", error);
      return null;
    }
  },
  updateUserAnimeStatus: async (animeListStatusDto) => {
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
      console.error("Error getting user anime:", error);
      return null;
    } finally {
    }
  },
};

export default UserApi;
