import axios from "axios";

const AnimeApi = {
  getByKeyword: async (keyword) => {
    const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
    try {
      const response = await axios.get(`${gatewayUrl}/app/anime`, {
        params: { keyword },
      });
      return response.data;
    } catch (error) {
      console.error("Error finding animes with polls by keyword:", error);
      throw error;
    }
  },
  getByYearAndSeason: async (year, season) => {
    try {
      const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
      const response = await axios.get(
        `${gatewayUrl}/app/anime/by-year-and-season/${year}/${season}`
      );
      return response.data;
    } catch (error) {
      console.error("Error finding animes with polls:", error);
      throw error;
    }
  },
};

export default AnimeApi;
