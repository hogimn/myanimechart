import axios from "axios";

const AnimeApi = {
  getAnimeWithPollByKeyword: async (keyword) => {
    const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
    try {
      const response = await axios.get(
        `${gatewayUrl}/query/anime/getAnimeWithPollByKeyword`,
        {
          params: { keyword },
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error getting anime with poll by keyword:", error);
      throw error;
    }
  },
  getAnimeWithPoll: async (year, season) => {
    try {
      const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
      const response = await axios.get(
        `${gatewayUrl}/query/anime/getAnimeWithPoll/${year}/${season}`
      );
      return response.data;
    } catch (error) {
      console.error("Error getting anime with poll:", error);
      throw error;
    }
  },
};

export default AnimeApi;
