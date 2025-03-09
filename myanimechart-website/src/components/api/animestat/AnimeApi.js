import axios from "axios";

const AnimeApi = {
  findAnimeWithPollByKeyword: async (keyword) => {
    const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
    try {
      const response = await axios.get(
        `${gatewayUrl}/query/anime/findAnimeWithPollByKeyword`,
        {
          params: { keyword },
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error finding anime with poll by keyword:", error);
      throw error;
    }
  },
  findAnimeWithPoll: async (year, season) => {
    try {
      const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
      const response = await axios.get(
        `${gatewayUrl}/query/anime/findAnimeWithPoll/${year}/${season}`
      );
      return response.data;
    } catch (error) {
      console.error("Error finding anime with poll:", error);
      throw error;
    }
  },
};

export default AnimeApi;
