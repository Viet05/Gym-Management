package com.group2.gymmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardStatsDTO {
    private Double totalRevenue;
    private Long activeMembers;
    private Long totalMembers;
    private Long visitsToday;
    private Double revenueTrend; // Placeholder
    private Double memberTrend; // Placeholder
}
