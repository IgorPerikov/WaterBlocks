package org.clayman.waterblocks.controller;

import org.clayman.waterblocks.domain.Result;
import org.clayman.waterblocks.domain.WaterArea;
import org.clayman.waterblocks.service.ResultService;
import org.clayman.waterblocks.service.WaterBlocksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class WebController {
    private ResultService resultService;
    private WaterBlocksService waterBlocksSolver;

    @Autowired
    public void setResultService(ResultService resultService) {
        this.resultService = resultService;
    }

    @Autowired
    public void setWaterBlocksSolver(WaterBlocksService waterBlocksSolver) {
        this.waterBlocksSolver = waterBlocksSolver;
    }

    @RequestMapping("/")
    public String welcomePage() {
        return "welcome_page";
    }

    @RequestMapping("/upload")
    public String upload() {
        return "upload";
    }

    @RequestMapping("/results")
    public String results(Model model) {
        model.addAttribute("results", resultService.getList());
        return "results";
    }

    @RequestMapping("/result")
    public String resultById(Model model, @RequestParam(value = "id", required = true) String id) {
        model.addAttribute("result", resultService.getById(Long.parseLong(id)));
        return "result";
    }

    @RequestMapping(value="/uploadFile", method=RequestMethod.POST)
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
                String sequence = br.readLine();

                Result result = new Result();
                result.setSequence(sequence);

                Set<WaterArea> waterAreas = waterBlocksSolver.foundWaterAreaBorders(sequence);
                for (WaterArea waterArea : waterAreas) {
                    waterArea.setResult(result);
                }

                result.setWaterAreas(waterAreas);
                result.setWaterCapacity(waterBlocksSolver.countWaterCapacity(sequence, result.getWaterAreas()));

                resultService.save(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/results";
    }

    @RequestMapping("/raw/results/{id}")
    @ResponseBody
    public Result getResult(@PathVariable long id) {
        return resultService.getById(id);
    }

    @RequestMapping("/grid/{id}")
    @ResponseBody
    public int[][] getGridResult(@PathVariable long id) {
        String sequence = resultService.getById(id).getSequence();
        int[][] array = waterBlocksSolver.createGridFromSequenceArray(waterBlocksSolver.getIntArrayFromSequence(sequence));
        return array;
    }
}

