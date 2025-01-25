import axios from "axios";

const AnimeApi = {
  searchAnimeByKeyword: async (keyword) => {
    try {
      const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
      const response = await axios.get(
        `${gatewayUrl}/query/animeStat/getAnimeStatsByKeyword`,
        {
          params: { keyword },
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error searching anime by title:", error);
      throw error;
    }
  },
};

export default AnimeApi;
