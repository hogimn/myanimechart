import apiClient from "./apiClient";

const UserApi = {
  getCurrentUser: async () => {
    try {
      const response = await apiClient.get(`/user`, {
        withCredentials: true,
      });
      return response.data;
    } catch (error) {
      console.error("Error finding user:", error);
      return false;
    }
  },
  getAnimeStatuses: async () => {
    try {
      const response = await apiClient.get(`/user/anime-statuses`, {
        withCredentials: true,
      });
      return response.data;
    } catch (error) {
      console.error("Error finding user anime status list:", error);
      return {};
    }
  },
  getAnimeStatusById: async (id) => {
    try {
      const response = await apiClient.get(`/user/anime-status/${id}`, {
        withCredentials: true,
      });
      return response.data;
    } catch (error) {
      console.error("Error finding user anime status:", error);
      return null;
    }
  },
  updateAnimeStatus: async (animeListStatus) => {
    if (animeListStatus.status === "Select") {
      animeListStatus.status = null;
    }

    if (animeListStatus.score === 0) {
      animeListStatus.score = null;
    }

    try {
      const response = await apiClient.post(
        `/user/anime-status/update`,
        animeListStatus,
        {
          withCredentials: true,
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error updating user anime status:", error);
      return null;
    }
  },
  deleteAnimeStatus: async (animeListStatus) => {
    try {
      const response = await apiClient.post(
        `/user/anime-status/delete`,
        animeListStatus,
        {
          withCredentials: true,
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error deleting user anime status:", error);
      return null;
    }
  },
};

export default UserApi;
