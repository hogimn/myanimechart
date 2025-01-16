import axios from "axios";

const AnimeStatApi = {
    searchAnimeByTitleStartingWith: async (title) => {
        try {
            const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
            const response = await axios.get(`${gatewayUrl}/query/animeStat/getAnimeStatsByTitleStartingWith`, {
                params: { title }
            });
            return response.data;
        } catch (error) {
            console.error("Error searching anime by title:", error);
            throw error;
        }
    }
}

export default AnimeStatApi;