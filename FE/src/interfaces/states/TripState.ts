interface attraction {
  attraction_id: number;
  thumbnail_image_url: string;
  name: string;
  address: string;
  avg_rating: string;
  avg_price: string;
}

interface TripState {
  tripInfo: {
    plan_id: number;
    start_region: string;
    start_at: string;
    end_at: string;
    title: string;
    total_estimated_budget: number;
    total_budget: number;
    status: string;
    daily_plans: {
      attractions: attraction[];
      order: number;
      daily_estimated_budget: number;
    }[];
  };
}

export type {TripState};
