import axios from "axios";

const AnimeApi = {
  searchAnimeByKeyword: async (keyword) => {
    const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
    try {
      const response = await axios.get(
        `${gatewayUrl}/query/animeStat/getAnimeStatByKeyword`,
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
