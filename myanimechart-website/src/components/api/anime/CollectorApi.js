import axios from "axios";

const CollectorApi = {
  findAllPollCollectionStatusWithAnime: async () => {
    const gatewayUrl = process.env.REACT_APP_GATEWAY_URL;
    try {
      const response = await axios.get(`${gatewayUrl}/app/pollCollect/status`);
      return response.data;
    } catch (error) {
      console.error("Error finding poll collection statuses:", error);
      throw error;
    }
  },
};

export default CollectorApi;
