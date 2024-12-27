import axios from "axios";

const AnimeStatApi = {
    fetchAnimeStats: async (year, season) => {
        try {
            const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
            const response = await axios.get(`${gatewayUrl}/query/animeStat/${year}/${season}`);
            return response.data;
        } catch (error) {
            console.error("Error fetching anime stats:", error);
            throw error;
        }
    }
}

export default AnimeStatApi;