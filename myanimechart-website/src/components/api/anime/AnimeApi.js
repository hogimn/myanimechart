import axios from "axios";

const AnimeApi = {
  findAnimesWithPollsByKeyword: async (keyword) => {
    const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
    try {
      const response = await axios.get(
        `${gatewayUrl}/app/anime/findAnimesWithPollsByKeyword`,
        {
          params: { keyword },
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error finding animes with polls by keyword:", error);
      throw error;
    }
  },
  findAnimesWithPollsByYearAndSeason: async (year, season) => {
    try {
      const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
      const response = await axios.get(
        `${gatewayUrl}/app/anime/findAnimesWithPollsByYearAndSeason/${year}/${season}`
      );
      return response.data;
    } catch (error) {
      console.error("Error finding animes with polls:", error);
      throw error;
    }
  },
};

export default AnimeApi;
