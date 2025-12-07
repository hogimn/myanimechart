import apiClient from "./apiClient";

const PollCollectApi = {
  getStatuese: async () => {
    try {
      const response = await apiClient.get(`/collect-poll/statuses`);
      return response.data;
    } catch (error) {
      console.error("Error finding poll collection statuses:", error);
      throw error;
    }
  },
};

export default PollCollectApi;
