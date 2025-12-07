import apiClient from "./apiClient";

const AnimeApi = {
  getByKeyword: async (keyword) => {
    try {
      const response = await apiClient.get(`/anime`, {
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
      const response = await apiClient.get(
        `/anime/by-year-and-season/${year}/${season}`
      );
      return response.data;
    } catch (error) {
      console.error("Error finding animes with polls:", error);
      throw error;
    }
  },
};

export default AnimeApi;
